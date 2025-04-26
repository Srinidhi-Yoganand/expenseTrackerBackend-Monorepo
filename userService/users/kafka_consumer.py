import json
from confluent_kafka import Consumer
from django.core.management.base import BaseCommand
from .models import UserInfo

class Command(BaseCommand):
  help = "Start Kafka consumer for user updates"

  def handle(self, *args, **kwargs):
    consumer = Consumer({
      'bootstrap.servers': 'localhost:9092',
      'group.id': 'userinfo-consumer-group',
      'auto.offset.reset': 'earliest'
    })

    consumer.subscribe(['user_service'])

    while True:
      msg = consumer.poll(1.0)
      if msg is None:
        continue
      if msg.error():
        print("Consumer error: {}".format(msg.error()))
        continue

      try:
        data = json.loads(msg.value().decode('utf-8'))
        UserInfo.objects.update_or_create(
          user_id=data['user_id'],
          defaults=data
        )
        print(f"User {data['user_id']} updated/created.")
      except Exception as e:
        print("Kafka consumer exception:", e)
