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

        // 3: TODO create a new message
        app.post("/messages", this::createMessageHandler);

        // 4: get all messages
        app.get("/messages", this::getAllMessagesHandler);

        // 5: TODO get a message by its message ID
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);

        // 6: TODO delete a message by its message ID
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);

        // 7: TODO update a message text by its message ID
        app.patch("/messages/{message_id}", this::updateMessageByMessageIdHandler);

        // 8: TODO get all messages written by a particular user
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
     * If accountService returns a null account (meaning posting a account was unsuccessful, the API will return a 400
     * message (client error).
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
     * If accountService returns a null account (meaning an account with a username:password key:value pair was 
     * not found, the API will return a 401 status code message (unauthorized)
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

    private void createMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{
        // 3: TODO create a new message
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.addMessage(message);
        if (createdMessage == null){
            context.status(400);
        } else {
            context.status(200);
            context.json(objectMapper.writeValueAsString(createdMessage));
        }
        // TODO Change all of the handlers to use try/catch after this one is working
        // try {
        //     Message message = objectMapper.readValue(context.body(), Message.class);
        //     Message createdMessage = messageService.addMessage(message);
        //     if (createdMessage == null){
        //         context.status(400);
        //     } else {
        //         context.status(200);
        //         context.json(objectMapper.writeValueAsString(createdMessage));
        //     }
        // } catch (JsonMappingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // } catch (JsonProcessingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }

    /*
     * Handler to get all messages from the database.
     * If messageService returns an empty list, then there are no messages in the database
     * and the response body will be empty
     * If messageService returns a list of Messages, then the database has messages in it
     * and the response body will be JSON representation of the list of messages.
     * In both cases, the API will return a status code of 200
     */
    private void getAllMessagesHandler(Context context){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Message> allMessages = messageService.getAllMessages();
        context.status(200);
        try {
            context.json(objectMapper.writeValueAsString(allMessages));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void getMessageByMessageIdHandler(Context context){
        // 5: TODO get a message by its message ID
    }

    private void deleteMessageByMessageIdHandler(Context context){
        // 6: TODO delete a message by its message ID
    }

    private void updateMessageByMessageIdHandler(Context context){
        // 7: TODO update a message text by its message ID
    }

    private void getAllMessagesByAccountIdHandler(Context context){
        // 8: TODO get all messages written by a particular user
    }
}