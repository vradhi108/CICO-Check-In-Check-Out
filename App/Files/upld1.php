<?php


     if (is_uploaded_file($_FILES['bill']['tmp_name'])) {
    $uploads_dir = './'.$_GET['path']."/";
                            $tmp_name = $_FILES['bill']['tmp_name'];
                            $pic_name = $_GET['fn'];
                            // $pic_name=str_replace("-","_",date("d-m-Y"). "-".date("h-i-sa"));
                            move_uploaded_file($tmp_name, $uploads_dir.$pic_name.".jpg");
                            }
               else{
                   echo "File not uploaded successfully.";
           }

   ?>