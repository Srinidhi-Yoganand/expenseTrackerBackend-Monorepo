from .message_util import MessagesUtil
from .regex_service import RegexService

from .langchain_service import LangchainService
from .models import Expense
import re

class MessageService:
    def __init__(self):
        self.message_util = MessagesUtil()
        self.regex_service = RegexService()
        self.langchain_service = LangchainService()
    
    def process_message(self, message):
        if self.message_util.is_bank_sms(message):
            return self.regex_service.extract_expense(message)
        return None

    def process_message_v2(self, message):
        parsed_result = self.langchain_service.parse_message(message)

        if parsed_result != "Not a bank message":
            return self.extract_expense(parsed_result)
        return None
    
    def extract_expense(self, parsed_result):
        amount_pattern = r"Amount: (\d+\.\d{2})"
        currency_pattern = r"Currency: (\w+)"
        merchant_pattern = r"Merchant: (.+)"

        amount_match = re.search(amount_pattern, parsed_result)
        currency_match = re.search(currency_pattern, parsed_result)
        merchant_match = re.search(merchant_pattern, parsed_result)

        amount = amount_match.group(1) if amount_match else None
        currency = currency_match.group(1) if currency_match else None
        merchant = merchant_match.group(1) if merchant_match else None

        return Expense(amount=amount, merchant=merchant, currency=currency)