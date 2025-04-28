import json
from confluent_kafka import Producer
from django.conf import settings
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .message_service import MessageService

class KafkaService:
  def __init__(self):
    self.kafka_broker = settings.KAFKA_BROKER
    self.kafka_topic = settings.KAFKA_TOPIC
    self.producer = Producer({
      'bootstrap.servers': self.kafka_broker,
      'client.id': 'django-kafka-producer',
    })

  def send_message(self, message):
    try:
      self.producer.produce(self.kafka_topic, value=json.dumps(message), callback=self.delivery_report)
      self.producer.flush()  
    except Exception as e:
      print(f"Error sending message to Kafka: {e}")

  def delivery_report(self, err, msg):
    if err is not None:
      print(f"Message delivery failed: {err}")
    else:
      print(f"Message sent: {msg.value()[:]}...")
      print(f"Message delivered to {msg.topic()} [{msg.partition()}]")