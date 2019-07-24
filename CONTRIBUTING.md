# How to Contribute to EonTimer
First off, thank you for taking the time to contribute! :+1: :tada: 

### Table of Contents

* [Code of Conduct](#code-of-conduct)
* [How to Contribute](#how-to-contribute)
  * [Discuss](#discuss)
  * [Create a Ticket](#create-a-ticket)
  * [Submit a Pull Request](#submit-a-pull-request)
* [Build from Source](#build-from-source)
* [Source Code Style](#source-code-style)

### Code of Conduct

This project is governed by the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md).
By participating you are expected to uphold this code.

### How to Contribute

#### Discuss

If you suspect an issue or would like to submit a feature request, perform a search in the
[GitHub issue tracker](https://github.com/dylmeadows/EonTimer/issues), using a few different keywords.
When you find related issues and discussions, prior or current, it helps you to learn and
it helps us to make a decision.

#### Create a Ticket

Reporting an issue or making a feature request is a great way to contribute. Your feedback
and the conversations that result from it provide a continuous flow of ideas. 

Before you create a ticket, please take the time to [research first](#discuss).

When ready create a ticket in the [GitHub issue tracker](https://github.com/dylmeadows/EonTimer/issues).

#### Submit a Pull Request

You can contribute a source code change by submitting a pull request.

1. For all but the most trivial of contributions, please [create a ticket](#create-a-ticket).
The purpose of the ticket is to understand and discuss the underlying issue or feature.
We use the GitHub issue tracker as the preferred place of record for conversations and
conclusions. In that sense discussions directly under a PR are more implementation detail
oriented and transient in nature.

1. Always check out the `master` branch and submit pull requests against it.

1. Use short branch names, based on the GitHub issue (e.g. `22276`) number followed by a succinct, 
lower-case, dash (-) delimited name, such as `fix-warnings`.

1. Choose the granularity of your commits consciously and squash commits that represent
multiple edits or corrections of the same logical change. See
[Rewriting History section of Pro Git](https://git-scm.com/book/en/Git-Tools-Rewriting-History)
for an overview of streamlining commit history.

1. Format commit messages using 55 characters for the subject line, 72 characters per line
for the description, followed by the issue fixed, e.g. `Closes gh-22276`. See the
[Commit Guidelines section of Pro Git](https://git-scm.com/book/en/Distributed-Git-Contributing-to-a-Project#Commit-Guidelines)
for best practices around commit messages and use `git log` to see some examples.

1. List the GitHub issue number in the PR description.

If accepted, your contribution may be heavily modified as needed prior to merging.
You will likely retain author attribution for your Git commits granted that the bulk of
your changes remain intact. You may also be asked to rework the submission.

If asked to make corrections, simply push the changes against the same branch, and your
pull request will be updated. In other words, you do not need to create a new pull request
when asked to make changes.

### Build from Source

See the [Build from Source](https://github.com/dylmeadows/EonTimer/wiki/Build-from-Source)
wiki page for instructions on how to check out, build, and import EonTimer source code into
your IDE.

### Source Code Style

Source code style is enforced using [ktlint](https://ktlint.github.io/) and [ktlint-gradle](https://github.com/jlleitschuh/ktlint-gradle).

To run ktlint use:

```bash
./gradlew ktlintCheck
```

ktlint is also capable of fixing code style problems. To remedy code style issues, use:

```bash
./gradlew ktlintFormat
```