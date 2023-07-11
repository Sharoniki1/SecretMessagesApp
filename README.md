# Secret Messages Application

![chat1](https://github.com/Morika1/SecretMessagesApp/assets/68810288/7675c034-758a-4246-a048-ef57d1bbd717)  
![encode1](https://github.com/Morika1/SecretMessagesApp/assets/68810288/a05d771f-9843-4a22-9072-0badd07e9092)
![permission](https://github.com/Morika1/SecretMessagesApp/assets/68810288/077c4f7b-53b4-473d-8c12-d0c5a8b5a742)  ![encode2](https://github.com/Morika1/SecretMessagesApp/assets/68810288/a7980e7c-e3db-42b2-81cf-164fffcbb20a)
![chat2](https://github.com/Morika1/SecretMessagesApp/assets/68810288/15d02794-a1c4-4a6b-9a36-6dda1b310a4c)
![chat3](https://github.com/Morika1/SecretMessagesApp/assets/68810288/dcbce461-b69c-4bc5-b4a2-e863fadcbfb9)  ![dialogbox](https://github.com/Morika1/SecretMessagesApp/assets/68810288/631bb942-5880-40f5-a1be-728671c5452d)

This application is a chat that its messages are encoded images with encrypted messages.
Each message being encrypted is shown in the chat and can be chose and decoded with its secrey key.

** Note: In order to execute this application with real-time Database to allow users to chat with each other firebase can be added to the project.

# Description

When the application is openning, the chat screen appears. 
On the top of the chat there is an 'Encode' button. 

- Clicking 'Encode' button will open encoding screen which allows user to choose image, and enter the secret message and key.
- Once the empty image is clicked - the application requests permission to access the galery.
- As soon as permission granted, the gallery will open and allow to choose an image.
- At the end of encryption the chat screen will appear again with the encrypted message in it.

Any encrypted image in the chat is clickable to decrypt. 
At the bottom of the chat screen there is a text field to enter a secret key for the chosen image. 
Next to the text field there is a 'Decode' button which is available only with the combination of chosing an image and providing secret key. 

- If the key is wrong, user will be notified to have the wrong key
- If the key matches the selected image, a dialog box will appear with the decrypted message.
- Accepting the message will close the message and remove it from the chat. 
  

  

  



