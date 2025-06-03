from langchain.llms import OpenAI
from langchain.prompts import PromptTemplate
from langchain.chains import LLMChain

class LangchainService:
  def __init__(self):
    self.llm = OpenAI(temperature=0)  
    self.setup_chain()

  def setup_chain(self):
    prompt_template = """
    You are an AI assistant that processes bank messages. 
    Extract the following details from the message if it is a bank message:
    1. Amount (Currency format like 100.00 or 2000)
    2. Currency (USD, INR, EUR, GBP, etc.)
    3. Merchant name (the name of the vendor where the transaction happened)

    If this is not a bank message, respond with "Not a bank message". 
    
    Message: "{message}"
    """

    prompt = PromptTemplate(input_variables=["message"], template=prompt_template)
    self.chain = LLMChain(llm=self.llm, prompt=prompt)

  def parse_message(self, message: str):
    result = self.chain.run(message=message)
    return result