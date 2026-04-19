rootProject.name = "cafeBot"

include(
    "modules:cafeBot-api-wrapper",
    "modules:meme-api-wrapper",
    "modules:i18n"
)

project(":modules:cafeBot-api-wrapper").name = "cafeBot-api-wrapper"
project(":modules:meme-api-wrapper").name = "meme-api-wrapper"
project(":modules:i18n").name = "i18n"
