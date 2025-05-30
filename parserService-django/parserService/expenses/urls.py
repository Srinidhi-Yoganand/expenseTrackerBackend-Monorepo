from django.urls import path
from django.http import JsonResponse
from .views import process_message_view

urlpatterns = [
  path("v1/ds/message", process_message_view, name="process_message"),
  path("health", lambda request: JsonResponse({"status": "ok"}), name="health_check"),
]