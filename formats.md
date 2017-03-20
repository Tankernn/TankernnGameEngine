# Tankernn Game Engine format specification

## models.json

Available options:

- name (string), used to guess the filenames of files not specified according to the format: <name> + (.obj | .png | S.png | N.png)
- id (int) <required>
- model (filename) <required>
- texture (filename) <required>
- normal (filename)
- specular (filename)
- shinedamper (float)
- reflectivity (float)
- refractivity (float)
- transparency (boolean)

## *.anim

name(string, no spaces) length(int)