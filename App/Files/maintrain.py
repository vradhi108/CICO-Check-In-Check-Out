import face_recognition as fr
import cv2
import numpy as np
import os

path = "./train/"

known_names = []
known_name_encodings = []

images = os.listdir(path)
for _ in images:
    image = fr.load_image_file(path + _)
    image_path = path + _
    encoding = fr.face_encodings(image)[0]

    known_name_encodings.append(encoding)
    known_names.append(os.path.splitext(os.path.basename(image_path))[0].capitalize())
file = open("arr", "wb")
# save array to the file
np.save(file, known_name_encodings)
# close the file
file.close
file1 = open("arr1", "wb")
# save array to the file
np.save(file1, known_names)
# close the file
file1.close
print(known_names)

