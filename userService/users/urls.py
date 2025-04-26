from django.urls import path
from .views import UserView, health_check

urlpatterns = [
  path('user/v1/getUser', UserView.as_view()),
  path('user/v1/createUpdate', UserView.as_view()),
  path('health', health_check),
]
