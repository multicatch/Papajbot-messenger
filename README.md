# Papajbot Messenger

A Messenger chatbot project written in Kotlin. 
This project was primarily used for (now dead) Facebook page, but you can use it to build your own chatbot.

This project does not contain any NLP or ML code. 
It is a framework used to communicate with Messenger API and respond to messages and user actions.

## Project structure

This project contains following Maven modules:

The most important and generic modules:
* `papajbot-web` - a module that is the glue and also contains the configuration of the chatbot.
* `papajbot-server-core` - a core module used to run a web server so the Messenger API can communicate with the bot and send messages.
* `papajbot-model` - the communication model for Messenger API.
* `papajbot-talk` - a module with base models for human-bot interaction.

The more specific use-case modules:
* `papajbot-youtube` - used to respond with a random YouTube video from a given youtube dump file (default is [`youtube_dump.json`](papajbot-youtube/src/main/resources/youtube_dump.json), which contains saved search results with YouTube API).
* `papajbot-video-responses` - a module with predefined video responses that will be randomly sent when a certain user message is detected.

## Running it

You can run it like a standard Java app. The `papajbot-web` builds two jars: one standard runnable jar and one with all dependencies packaged.
To run it as standalone app please use the second one.

To test the bot you will need to configure all variables from [Main.kt](papajbot-web/src/main/kotlin/io/github/multicatch/papajbot/Main.kt).

Those include variables with Facebook page access token, webhook response and SSL config.

## Configuration

You can see the example project configuration in the [Main.kt](papajbot-web/src/main/kotlin/io/github/multicatch/papajbot/Main.kt) file.
You can create your own chatbot configuration basing on the one in the `papajbot-web` module.

You will need to:
* get a Facebook page access token
* configure webhook for Messenger API (along with the whole server)
* configure HTTP client to send configuration to the Messenger API
* configure possible responding strategy

For some basics on how a Facebook chatbot works, see [this official guide](https://developers.facebook.com/docs/messenger-platform/getting-started/quick-start/).

Please note that in the above mentioned file there is a list variable named `handlers` 
that is used to configure all handlers for responding.

This is a chain of handlers and when Facebook sends an event, the first matching handler will be used.

## License

All code stored here is licensed under MIT License.
