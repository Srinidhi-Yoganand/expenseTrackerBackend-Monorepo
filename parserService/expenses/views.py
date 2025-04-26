from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import json
from .message_service import MessageService

@csrf_exempt
def process_message_view(request):
  if request.method == 'POST':
    data = json.loads(request.body)
    message = data.get("message", "")
    service = MessageService()
    expense = service.process_message(message)
    if expense:
      return JsonResponse(expense.serialize())
    else:
      return JsonResponse({"error": "Not a bank message"}, status=400)
  return JsonResponse({"error": "Invalid method"}, status=405)