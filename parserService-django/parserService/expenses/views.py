from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import json
from .message_service import MessageService
from .kafka_producer import KafkaService

@csrf_exempt
def process_message_view(request):
  if request.method == 'POST':
    data = json.loads(request.body)
    message = data.get("message", "")
    
    service = MessageService()
    expense = service.process_message(message)
    
    if expense:
      serialized_data = expense.serialize()

      user_id = request.headers.get('x-user-id', None)
      if user_id:
          serialized_data['user_id'] = user_id

      kafka_service = KafkaService()
      kafka_service.send_message(serialized_data)

      return JsonResponse(serialized_data)
    else:
      return JsonResponse({"error": "Not a bank message"}, status=400)

  return JsonResponse({"error": "Invalid method"}, status=405)