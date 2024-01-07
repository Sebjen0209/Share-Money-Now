const functions = require("firebase-functions");
const admin = require("firebase-admin");
const { logger } = require("firebase-functions");

admin.initializeApp();

console.log(signature, "Initializing function");

exports.sendNotification = functions.https.onCall((data, context) => {
    console.log(signature, "Request received", data);
    logger.debug("Request received", data);

    console.log(signature, "Request authenticated");
    logger.debug("Request authenticated");

    const { title, body, tokens } = data;

    console.log(signature, "Data received", data);
    logger.debug("Data received", data);

    const messages = tokens.map((token) => ({
        data: {
            title: title,
            body: body,
        },
        token: token,
    }));

    logger.debug("Messages to send", messages);

    return admin.messaging().sendAll(messages)
        .then((response) => {
            logger.info("Notification sent");
            return { success: true, response: response.responses };
        })
        .catch((e) => {
            logger.error("Notification not sent", e.message);
            throw new functions.https.HttpsError("unknown", e.message, e);
        });
});
