# ASCII-fy
## Description
Takes a jpg image and converts it into an "ASCII-fied" black and white version of that image

## Requirements
Java 1.8
Maven

## Usage
`$ java ImageImporter`

## Test Case Example
1. Run `$mvn test`
2. View converted grayscale and ASCII images, along with original at: `/ASCII-fy/target/test-classes`

## TODO
1. Add arguments parser and allow user to specify input and output file name
2. Add support for images with alpha channels (e.g. pngs).
3. Refactor ImageImporter. Embed call to toGrayscale() within toASCII().
3. Add more test cases
