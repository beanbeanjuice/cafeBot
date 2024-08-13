rootProject.name = "cafeBot"

include(
    "wrappers:cafe-api-wrapper",
    "wrappers:kawaii-api-wrapper"
)

project(":wrappers:cafe-api-wrapper").name = "cafe-api-wrapper"
project(":wrappers:kawaii-api-wrapper").name = "kawaii-api-wrapper"
