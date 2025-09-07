# Contribution Guidelines

I welcome contributions from everyone, so please feel free to contribute!

## Getting Started

1. Determine which [issue](https://www.github.com/beanbeanjuice/cafeBot/issues) you want to work on. If an issue does not yet exist, please make it.
2. Leave a comment on the issue that you will be working on, that you have claimed it.
3. First, create a fork of the repository.
4. Create your edits.
5. Make a PR with your fork, and merge it into the `master` branch!
6. Finally, fill out the PR template. If you do not fill it out properly, I will let you know.

## Pull Request Etiquette

1. **Use [conventional commits](https://www.conventionalcommits.org).** Your PR tests will fail unless you do this.

2. **PR titles must follow the correct format.** You must use the following format for your PR titles:

```
(fix/feat/chore): [GH-ZZZ] Some Description
```

For example, I would use this.

```
feat: [GH-620] Remove Swears from Serve Command
```

3. **Be kind, respectful, and patient.** I am the sole overseer that determines what gets added, when, and where. I know my comments and requests might be a little too much, but please bare with me.

## Testing Guidelines

1. **All previous tests must pass.** If something inherently breaks, notify the person that wrote that code. Otherwise, if your code broke the test, fix the code, or fix the test.
2. **New code, new tests.** If you write new code, you more likely than not should be writing new tests as well. Some things can't be tested, such as mimicking a Discord user, but things like making sure your parser does what it is supposed to should have a test.
3. We are not aiming for a specific coverage percentage, but just try to make sure all parts that need testing should be tested properly.

## Philosophy

* **If it might be null, say so.** For return values in functions, make sure to use the `Optional` package. If the value could be null within function parameters, make sure to annotate it with @Nullable.

* **Clean code.** Try to keep your functions short. I know it's not always possible, but please try.

* **Asynchronous always.** When things might take a while, make sure not to do blocking calls. I want the bot to be as fast as possible as much as possible.

* **Leave comments.** Please leave comments whenever something you wrote might be ambiguous, or if you need to tell us why you chose to do something the way you did.

* **Reference documentation.** If you added something or changed something, please refer to the documentation. For example, you may need to add in the `README.md` a new command that you just created.
