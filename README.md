# Information Retrieval coursework

Java assignments using the Cranfield document collection.
Source, reports, and captured output are retained for reference.

| Assignment | Contents |
| --- | --- |
| [Assignment 1](Assignment%201/ReadMe.txt) | Tokenization and stemming. |
| [Assignment 2](Assignment%202/ReadMe.txt) | Index construction and compression, with Stanford CoreNLP and Apache Commons dependencies. |
| [Assignment 3](Assignment%203/ReadMe.txt) | Query processing and retrieval experiments. |

## Local tokenization example

With a JDK providing `javac` and `java`, run from the repository root:

```sh
mkdir -p out/assignment1
javac -d out/assignment1 "Assignment 1/Stemmer.java" "Assignment 1/tokenization.java"
java -cp out/assignment1 tokenization "Assignment 1/Cranfield"
```

The corpus directory must contain the Cranfield documents.
The later assignment guides record the original university environment, including `/usr/local/corenlp341`, server names, and personal paths.
Replace those with your own corpus, query, stopword, library, and output locations.
They are separate Java programs; do not compile all assignments into one class directory.
The original CoreNLP 3.4.1-era classpaths are not a verified setup for a current CoreNLP release.
