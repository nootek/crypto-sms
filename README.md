1)Compressing protocol
Hours:50
Problem:
1)bad exception handling(rewrite exception hierarchy later)
2)some compress and decompress functions in archivers are very complex,need to separate them
3)in dictionary class some problems can occur with relative path(FileInputStream)
4)in compression protocol RuntimeException can occur when list of archiver data to compare is empty(behaviour of java
api for collection min)
