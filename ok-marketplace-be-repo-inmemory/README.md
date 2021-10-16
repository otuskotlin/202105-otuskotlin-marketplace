## Настройки подключения
1. Зарегистрируйтесь/войдите в аккаунт AWS
2. Создайте файл <i>credentials</i> 
````
Windows - C:\Users\USERNAME\.aws\credentials
Linux - ~/.aws/credentials
````
2. В текстовом редакторе в созданном файле введите
````
   [default]
   aws_access_key_id = YOUR_AWS_ACCESS_KEY_ID
   aws_secret_access_key = YOUR_AWS_SECRET_ACCESS_KEY
````
где YOUR_AWS_ACCESS_KEY_ID и YOUR_AWS_SECRET_ACCESS_KEY - уникальный ID и ключ доступа к AWS, 
<br>создается в личном кабинете My Security Credentials -> Access Keys
