FROM python:3.11-slim
LABEL authors="srinidhi"

RUN apt-get update && apt-get install -y \
    pkg-config \
    libmariadb-dev \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY requirements.txt /app/

RUN pip install --no-cache-dir -r requirements.txt

COPY . /app/

EXPOSE 9810

CMD ["sh", "-c", "python manage.py migrate && python manage.py runserver 0.0.0.0:9810"]
