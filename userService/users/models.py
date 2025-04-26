from django.db import models

class UserInfo(models.Model):
  user_id = models.CharField(primary_key=True, max_length=255)
  first_name = models.CharField(max_length=255)
  last_name = models.CharField(max_length=255)
  phone_number = models.BigIntegerField()
  email = models.EmailField()
  profile_pic = models.TextField(null=True, blank=True)

  def __str__(self):
    return f"{self.user_id} - {self.first_name} {self.last_name}"