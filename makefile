# Makefile for CS455 HW1-PC

PACKAGES = \
	cs455/scaling/server \
	cs455/scaling/client \
	cs455/scaling/threadpool \
	cs455/scaling/tasks

JC = javac
JVM = java

SRC = ./

JFLAGS = -g

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

EMPTY = 

PACKAGEDIRS = $(addprefix $(SRC), $(PACKAGES))
PACKAGEFILES = $(subst $(SRC), $(EMPTY), $(foreach DIR, $(PACKAGEDIRS), $(wildcard $(DIR)/*.java)))
#PACKAGEFILES = $(foreach DIR, $(PACKAGES), $(wildcard $(DIR)/*.java))

default: class_files

all: class_files

class_files: $(PACKAGEFILES:.java=.class)

clean:
	$(foreach DIR, $(PACKAGEDIRS), $(RM) $(DIR)/*.class)

