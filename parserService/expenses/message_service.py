from .message_util import MessagesUtil
from .regex_service import RegexService

class MessageService:
    def __init__(self):
        self.message_util = MessagesUtil()
        self.regex_service = RegexService()
    
    def process_message(self, message):
        if self.message_util.is_bank_sms(message):
            return self.regex_service.extract_expense(message)
        return None
