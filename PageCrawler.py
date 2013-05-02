'''
quick program to crawl data from the web given its url
@usage: python ME input outputDir

@param input: 
the url list in the format
date 1
URL 1
------------
date 2
URL 2
------------
@param outputDir: the directory where the downloaded files 
'''
import sys, os
#sys.path.add('/home/gtran/local/lib/python/')
import time
import socks
import socket
import random
from threading import Timer

from cookielib import CookieJar
useProxy = False
default_timeout = 5
socket.setdefaulttimeout(default_timeout)
port = random.randint(0, 830) + 40000
if useProxy:
    socks.setdefaultproxy(socks.PROXY_TYPE_SOCKS5, "np.l3s.uni-hannover.de", port)
socket.socket = socks.socksocket

import urllib2, urllib


directory = "/workspaces/wksum-project/Data/InternetData/LibyaWar_cnn/"
localDir = directory + "/http_news/"
if not os.path.exists(localDir):
    os.makedirs(localDir);
    
    
if __name__=="__main__":
    agen =  'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0'
    fi = open (directory +"/urls_for_news.txt", "r")
    offset = 0
    for url in fi:
        if url.find("http") ==-1:
             continue
        offset+=1
        #if (offset < int(sys.argv[1])):
        #    continue
        url = url.strip()
        print offset, url
        Error_count = 0
        repeat = True
        while (repeat):
            repeat = False
            try:
                if useProxy:
                    port = random.randint(0,830) + 40000
                    socks.setdefaultproxy(socks.PROXY_TYPE_SOCKS5, "np.l3s.uni-hannover.de", port)
                    print "np.l3s.uni-hannover.de", port
                request = urllib2.Request(url, headers={'User-agent':agen})
                request.add_header('User-agent', agen)
                cj = CookieJar()
                opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj))
                try:
                    fh = opener.open(request, timeout=5)
                    data = fh.read()
                    if data !=-1:
                        fo = open (os.path.join(localDir, str(offset) + ".htm"), "w")
                        fo.write(data)
                        fo.close()
                    else:
                        Error_count +=1
                        if (Error_count <2):
                            repeat = True
                except Exception, e:
                    print e, Error_count
                    Error_count +=1
                    if (Error_count <2):
                        repeat = True
                time.sleep(1.0 * random.randint(0,10)/5)
                
            except Exception, e:
                print e, Error_count
                Error_count +=1
                if (Error_count <2):
                    repeat = True
        
    fi.close()

