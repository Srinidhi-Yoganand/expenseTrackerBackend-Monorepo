from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import UserInfo
from .serializers import UserInfoSerializer
from django.http import JsonResponse

class UserView(APIView):
  
  def get(self, request):
    user_id = request.query_params.get("user_id")
    try:
      user = UserInfo.objects.get(user_id=user_id)
      serializer = UserInfoSerializer(user)
      return Response(serializer.data, status=status.HTTP_200_OK)
    except UserInfo.DoesNotExist:
      return Response(status=status.HTTP_404_NOT_FOUND)

  def post(self, request):
    serializer = UserInfoSerializer(data=request.data)
    if serializer.is_valid():
      serializer.save()  # Updates or creates
      return Response(serializer.data, status=status.HTTP_200_OK)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

def health_check(request):
  return JsonResponse({"healthy": True}, status=200)
