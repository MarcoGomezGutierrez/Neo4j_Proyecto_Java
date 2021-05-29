# -*- coding: utf-8 -*-
"""
Created on Wed May 12 22:36:18 2021

Programa que transforma de String->Voz

Instalar si no tienes Python en la store de Microsoft

Instalar en la cmd el comando:
    
    pip install pypiwin32
    
Para obtener la libreria de Python

Ejecutar el comando:
    
    python C:/Users/marco/eclipse-workspace/Neo4jProyectoFinal/src/main/java/phyton/voz/Voz.py 'Hola'

De esta forma hablara.

@author: marco
"""

import win32com.client
from sys import argv
script, nombre = argv
speaker = win32com.client.Dispatch("SAPI.SpVoice")
speaker.Speak(nombre)