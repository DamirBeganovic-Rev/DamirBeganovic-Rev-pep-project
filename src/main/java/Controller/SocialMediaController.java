package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // 1: create a new account
        app.post("/register", this::registerHandler);

        // 2: process login
        app.post("/login", this::loginHandler);

        // 3: create a new message
        app.post("/messages", this::createMessageHandler);

        // 4: get all messages
        app.get("/messages", this::getAllMessagesHandler);

        // 5: get a message by its message ID
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);

        // 6: delete a message by its message ID
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);

        // 7: update a message text by its message ID
        app.patch("/messages/{message_id}", this::updateMessageByMessageIdHandler);

        // 8: get all messages written by a particular user
        app.get("accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /*
     * Handler to post a new Account
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If accountService returns a null object (meaning posting a account was unsuccessful, the API will return a 400
     * status code (client error).
     * If registration is successful, API returns a 200 status code
     */
    private void registerHandler(Context context) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Account account = objectMapper.readValue(context.body(), Account.class);
            Account registeredAccount = accountService.addAcount(account);
            if (registeredAccount == null){
                context.status(400);
            } else {
                context.status(200);
                context.json(objectMapper.writeValueAsString(registeredAccount));
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /*
     * Handler to log into an Account
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If accountService returns a null object (meaning an account with a username:password key:value pair was 
     * not found, the API will return a 401 status code (unauthorized)
     * If the login is successful, API will return a 200 status code
     */
    private void loginHandler(Context context) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Account account = objectMapper.readValue(context.body(), Account.class);
            Account loggedinAccount = accountService.login(account);
            if (loggedinAccount == null) {
                context.status(401);
            } else {
                context.status(200);
                context.json(objectMapper.writeValueAsString(loggedinAccount));
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /*
     * Handler to post a new Message
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into a Message object.
     * If messageService returns a null object (meaning posting a message was unsuccessful, the API will return a
     * 400 status code (client error).
     * If the message is created successfully, API returns a 200 status code
     */
    private void createMessageHandler(Context context) {    
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Message message = objectMapper.readValue(context.body(), Message.class);
            Message createdMessage = messageService.addMessage(message);
            if (createdMessage == null){
                context.status(400);
            } else {
                context.status(200);
                context.json(objectMapper.writeValueAsString(createdMessage));
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
    }

    /*
     * Handler to get all messages from the database.
     * If messageService returns an empty list, then there are no messages in the database
     * and the response body will be empty
     * If messageService returns a list of Messages, then the database has messages in it
     * and the response body will be a JSON representation of the list of messages.
     * In both cases, the API will return a 200 status code
     */
    private void getAllMessagesHandler(Context context){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Message> allMessages = messageService.getAllMessages();
            context.status(200);
            context.json(objectMapper.writeValueAsString(allMessages));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /*
     * Handler to get a specific message from the database by its message_id.
     * If messageService returns null, then there are no messages in the database with the
     * specified message_id and the response body will be empty
     * If messageService returns a Message, then the database has a message with the specified
     * message_id and the response body will be a JSON representation of the message.
     * In both cases, the API will return a 200 status code
     */
    private void getMessageByMessageIdHandler(Context context){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message retrievedMessage = messageService.getMessageByMessageId(message_id);
            context.status(200);
            if (retrievedMessage != null) {
                context.json(objectMapper.writeValueAsString(retrievedMessage));
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /*
     * Handler to delete a message from the database by its message_id.
     * If messageService returns null, then there are no messages in the database with the
     * specified message_id and the response body will be empty
     * If messageService returns a Message, then the database has a message with the specified
     * message_id and the response body will be a JSON representation of the message.
     * In both cases, the API will return a 200 status code
     */
    private void deleteMessageByMessageIdHandler(Context context){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessageByMessageId(message_id);
            context.status(200);
            if (deletedMessage != null){
                    context.json(objectMapper.writeValueAsString(deletedMessage));
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /*
     * Handler to update a message in the database by its message_id.
     * If messageService returns null, then there are no messages in the database with the
     * specified message_id and the response body will be empty.
     * The API will return a 400 status code  if unsuccessful
     * If messageService returns a Message, then the database has a message with the specified
     * message_id and the response body will be a JSON representation of the updated message.
     * The API will return a 200 status code if successful
     */
    private void updateMessageByMessageIdHandler(Context context){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message message = objectMapper.readValue(context.body(), Message.class);
            Message updatedMessage = messageService.updateMessageByMessageId(message_id, message);
            if (updatedMessage == null){
                context.status(400);
            } else {
                context.status(200);
                context.json(objectMapper.writeValueAsString(updatedMessage));
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } 
    }

    /*
     * Handler to get all messages from the database from a specified user.
     * If messageService returns an empty list, then there are no messages in the database
     * from that user and the response body will be empty
     * If messageService returns a list of Messages, then the database has messages in it
     * from the specified user and the response body will be a JSON representation of the 
     * list of messages.
     * In both cases, the API will return a 200 status code
     */
    private void getAllMessagesByAccountIdHandler(Context context){        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            int account_id = Integer.parseInt(context.pathParam("account_id"));
            List<Message> allMessages =  messageService.getAllMessagesByAccountId(account_id);
            context.status(200);
            context.json(objectMapper.writeValueAsString(allMessages));
        }catch (JsonMappingException e) {
            e.printStackTrace();
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}