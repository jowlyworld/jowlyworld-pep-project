package Controller;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;

public class SocialMediaController {
    AccountService accountService = new AccountService(new AccountDAO());
    MessageService messageService = new MessageService(new MessageDAO(), new AccountDAO());

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{id}", this::getMessageById);
        app.delete("/messages/{id}", this::deleteMessage);
        app.patch("/messages/{id}", this::updateMessage);
        app.get("/accounts/{id}/messages", this::getMessagesByUser);

        return app;
    }

    private void registerHandler(Context ctx) throws SQLException {
        Account acc = ctx.bodyAsClass(Account.class);
        Account created = accountService.register(acc);
        if (created != null) ctx.json(created);
        else ctx.status(400);
    }

    private void loginHandler(Context ctx) throws SQLException {
        Account acc = ctx.bodyAsClass(Account.class);
        Account found = accountService.login(acc);
        if (found != null) ctx.json(found);
        else ctx.status(401);
    }

    private void createMessage(Context ctx) throws SQLException {
        Message msg = ctx.bodyAsClass(Message.class);
        Message created = messageService.create(msg);
        if (created != null) ctx.json(created);
        else ctx.status(400);
    }

    private void getAllMessages(Context ctx) throws SQLException {
        ctx.json(messageService.getAll());
    }

    private void getMessageById(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message msg = messageService.getById(id);
        if (msg != null) ctx.json(msg);
        else ctx.json("");
    }

    private void deleteMessage(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message deleted = messageService.delete(id);
        if (deleted != null) ctx.json(deleted);
        else ctx.json("");
    }

    private void updateMessage(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message incoming = ctx.bodyAsClass(Message.class);
        Message updated = messageService.update(id, incoming.getMessage_text());
        if (updated != null) ctx.json(updated);
        else ctx.status(400);
    }

    private void getMessagesByUser(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ctx.json(messageService.getByUser(id));
    }
}