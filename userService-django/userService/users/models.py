from django.db import models

class UserInfo(models.Model):
  user_id = models.UUIDField(primary_key=True)
  first_name = models.CharField(max_length=255, null=True, blank=True)
  last_name = models.CharField(max_length=255, null=True, blank=True)
  email = models.EmailField(null=True, blank=True)
  phone_number = models.CharField(max_length=20, null=True, blank=True)

  def __str__(self):
    return f"{self.user_id} - {self.first_name} {self.last_name}"