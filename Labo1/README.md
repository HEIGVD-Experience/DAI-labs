# Kuby - Transform text or music into stunning colorful images

![Logo](images/kuby1000x200.png)

## Description
Kuby is a command-line interface (CLI) that enables the creation of captivating images from text files (.txt) or music files (.mp3).

## Installation
Download the .jar file into the lastest [release](https://github.com/truebguillaume/Kuby/releases/tag/v1.0.0) and run the command explained into the Commands chapter. You can also download the source code and build the code with Maven but make sure to have included all dependencies.

## Usage
Kuby offers two main commands for conversions: text-to-image and music-to-image.

## Commands

### Text to image

```sh
java -jar <path-to-jar> text -i <inputFile> -o <outputFile>
```

### Music to image

```sh
java -jar <path-to-jar> music -i <inputFile> -o <outputFile>
```

### Required options

- `-i, --input <outputFile>`: Specifies the path to the input file containing the text or music to be converted.
- `-o, --output <outputFile>`: Specifies the path to the folder where the generated image will be saved.

### Not required options
If no options are provided, default values are used for each of them:

**Color**: Specifies the primary color of the generated image.

- Tag: `-c, --color`
- Values: `<color> [blue, green, yellow, red, random]`
- Default value: `random`

**Height**: Specifies the height of the generated image in pixels.

- Tag: `-h, --height`
- Default value: `100`

**Width**: Specifies the width of the generated image in pixels.

- Tag: `-w, --width`
- Default value: `100`

## Examples

### Conversion from a text to an image (default value).
```sh
java -jar kuby.jar text -i input.txt -o output
```
Result :   

![Sortie](images/kuby100x100.png)
### Conversion from a music to a yellow image with a height of 100 and a width of 200.

```sh
java -jar kuby.jar music -i input.txt -o output -c yellow -h 100 -w 200
```
Result :

![Sortie](images/kuby200x100.png)
