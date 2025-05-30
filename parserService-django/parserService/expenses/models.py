from django.db import models

class Expense(models.Model):
  amount=models.CharField(max_length=20, null=True, blank=True)
  merchant=models.CharField(max_length=255, null=True, blank=True)
  currency=models.CharField(max_length=10, null=True, blank=True)

  def serialize(self):
    return {
      "amount": self.amount,
      "merchant": self.merchant,
      "currency": self.currency
    }