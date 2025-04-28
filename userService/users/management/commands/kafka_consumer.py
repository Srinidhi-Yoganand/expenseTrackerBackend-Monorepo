import json
from confluent_kafka import Consumer
from django.core.management.base import BaseCommand
from ...models import UserInfo

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
        print(f"Consumer error: {msg.error()}")
        continue

      raw_data = msg.value().decode('utf-8')
      # print(f"Raw message received: {raw_data}")  

      try:
        data = json.loads(raw_data)
        # print(f"Parsed message data: {data}")  

        user_info_data = {
          'user_id': data.get('user_id'),
          'first_name': data.get('first_name') or "",  
          'last_name': data.get('last_name') or "",   
          'email': data.get('email'),
          'phone_number': data.get('phone_number') or "",  
        }

        if not user_info_data.get('user_id'):
          print("Received message doesn't contain 'user_id'. Skipping message.")
          continue

        UserInfo.objects.update_or_create(
          user_id=user_info_data['user_id'],
          defaults=user_info_data
        )
        print(f"User {user_info_data['user_id']} updated/created.")
      except Exception as e:
        print(f"Kafka consumer exception: {e}") 
        print(f"Failed message: {raw_data}")
