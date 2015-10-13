#!usr/bin/python3
# -*- coding: utf-8 -*- 

import socket
import sys

IP, PORT = sys.argv[1].split(':')
BUF_SIZE = 8192

sock = socket.socket()
sock.connect((IP, int(PORT)))
print("Client running")

cur_dir = "root/"

while True:
	inp = input(cur_dir + ' &: ') + "\n"
	sock_count = sock.send(inp.encode('utf-8'))
	data = []
	# while True:
	buf = sock.recv(BUF_SIZE).strip().decode("utf-8")
	inp = inp.strip().split()
	if inp[0] == "exit":
		sock.close()
		break
	elif inp[0] == "cd":
		cur_dir = buf.strip()
	else:
		print(buf)
