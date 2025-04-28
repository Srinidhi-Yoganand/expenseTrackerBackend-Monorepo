# Use the official Python image as a base image
FROM python:3.11-slim

# Install dependencies for mysqlclient (pkg-config, libmariadb-dev, etc.)
RUN apt-get update && apt-get install -y \
    pkg-config \
    libmariadb-dev \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the requirements file
COPY requirements.txt /app/

# Install the Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code
COPY . /app/

# Expose the port the app will run on
EXPOSE 9810

# Command to run the Django application
CMD ["sh", "-c", "python manage.py migrate && python manage.py runserver 0.0.0.0:9810"]
