import re
from .models import Expense

class RegexService:
  def extract_expense(self, message):
    amount_pattern = r'(\d+(?:\.\d{1,2})?)\s?(USD|INR|EUR|GBP|CAD)?'
    merchant_pattern = r'at\s([\w\s]+)'
    currency_pattern = r'(USD|INR|EUR|GBP|CAD)'

    amount_match = re.search(amount_pattern, message)
    merchant_match = re.search(merchant_pattern, message)
    currency_match = re.search(currency_pattern, message)

    amount = amount_match.group(1) if amount_match else None
    currency = currency_match.group(1) if currency_match else None
    merchant = merchant_match.group(1).strip() if merchant_match else None

    return Expense(amount=amount, merchant=merchant, currency=currency)
