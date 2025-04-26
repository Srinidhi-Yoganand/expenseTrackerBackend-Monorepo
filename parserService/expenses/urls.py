from django.urls import path
from .views import process_message_view

urlpatterns = [
  path("process-message/", process_message_view, name="process_message"),
]