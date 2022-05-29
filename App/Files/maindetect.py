import face_recognition as fr
import cv2
import numpy as np
import os
import sys
test_image = "./test/" + sys.argv[1]
image = cv2.imread(test_image)
# image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
file2 = open("arr1", "rb")
#read the file to numpy array
arr2 = np.load(file2)
face_locations = fr.face_locations(image)
face_encodings = fr.face_encodings(image, face_locations)
file = open("arr", "rb")
#read the file to numpy array
arr1 = np.load(file)
for (top, right, bottom, left), face_encoding in zip(face_locations, face_encodings):
    matches = fr.compare_faces(arr1, face_encoding)
    name = ""

    face_distances = fr.face_distance(arr1, face_encoding)
    best_match = np.argmin(face_distances)
    
    if matches[best_match]:
        name = arr2[best_match]

    cv2.rectangle(image, (left, top), (right, bottom), (0, 0, 255), 2)
    cv2.rectangle(image, (left, bottom - 15), (right, bottom), (0, 0, 255), cv2.FILLED)
    font = cv2.FONT_HERSHEY_DUPLEX
    cv2.putText(image, name, (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)
    print(name)


