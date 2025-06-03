from django.urls import path
from django.http import JsonResponse
from .views import process_message_view, process_message_view_v2

urlpatterns = [
  path("v1/ds/message", process_message_view, name="process_message"),
  path("v2/ds/message", process_message_view_v2, name="process_message_v2"),
  path("health", lambda request: JsonResponse({"status": "ok"}), name="health_check"),
]