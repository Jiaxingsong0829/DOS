----------------------------------------------------------------------------------------
To run the program
----------------------------------------------------------------------------------------
	This is a SBT project. There are two dependency libraries 
	
		libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.13"
	
		libraryDependencies += "com.typesafe.akka" % "akka-remote_2.11" % "2.3.13"

	The program can both run as a server and a client.
	
	To run a server:
		SBT		(Into the SBT environment)
		> run [k]  	([k] is an argument for server indicating the number of heading 0)
	
	To run a client:
		SBT
		> run [ip]	([ip] is the address of the server. The sever listening to port 9999 which is hard coded in the code.)
	


----------------------------------------------------------------------------------------
The Work Unit
----------------------------------------------------------------------------------------
	On the server, the work unit is 10,000 
	
	We tried work unit of 100 1,000 10,000 and 100,000. 

	The system is faster using 10,000.



----------------------------------------------------------------------------------------
Result
----------------------------------------------------------------------------------------

	The program is tested on two 4-core machines using 8 working actors.

	The result of 4 heading 0s.
	
	zhihuangK^(		0000aa51c76f356ddfe2347b0b5dd8f2e499e212484807deaf58e1b69778990b
	zhihuangvN/		0000fb87ada29df68f069d4cceb0f1fb96949ce343993e71e2da13bbf922da30
	zhihuangM!3		00008719b500da85d45327bdc2b3d2ad72c2d6ed826e15f681f146077eaf7550
	zhihuangB2@		000088dc3e07eac91305643d77a501f2eb864b9ae6ce378540aa283bced8a1a2
	zhihuang#%B		0000ffdc1d85c7fb9fb5c4d3c5102a19124027e71a6ce62ac917638a206ea989
	zhihuangS\O		0000d00fd879604cb170ffe7a225bf57046aa6fd55917bf26c5dea49c25e9dc9
	zhihuang8La		0000a37e04e0a505f0685154cd8c8c60b4660a96c970cc5b2d44da83c9b8b1d9
	zhihuangYHc		00002a70a90cfac483c8ee9cfa0f13d9334d8c6ca8488617ab87356f3b12fbb6
	zhihuangjgd		000045eedc4268b6c7b3fd4cac6e8a24eea996096ce2e0f5fc954c176ccd8f80
	zhihuangzHj		0000201e68e4321d8b0f09fd04e66cedf38522362caaaa17f817c7c155d35601
	zhihuang1jl		000081c16245031fd28ec7f70b03f69d29bd1b40981aad91760fc44de6017219
	zhihuangWDv		0000a27930d86e59c8fa7d5c397298645da2977b5a0b7f60b377f98a5d1f6d02
	zhihuangiK~		0000e6ed4e988dbffc465211f669e25a046d020353c9b55308bd39dfe031574b
	zhihuang#}j"		0000301aee1be2a90ef5c49b791321f7404ad59a4ad68605c075b86021daa174
	zhihuangZ1r"		00008bc70288372c177ef6e7086bcc70c2bd4ab1dfbedffd560a23b6375d249e
	zhihuang;(v"		0000f20f0085257cf5de8f7af20222cbdb5d41383d1c9497aa59e396950f1263
	zhihuangW;w"		0000671dbc68e3caf0f541a1f773d3da7296182a6f2dd883af359f98b5ef9678
	zhihuangw:u"		0000bded89faa9e812a872de023c5714be77a85929680c127701abea6ae5f869
	zhihuang)C$#		0000a8aa68d68579d62b66bffd487a965414934f986efd6e935a2eb0c1df0c4c
	zhihuang|l$#		00003178602e07f5bf948786275b4eb318d2715cb87b12f93b14fa4579f487dc
	zhihuangFU5#		00004ba3b2eff72813f7c45ede2626bdcc633e1ad7a0be20e9d8b122c8d6ba2b
	zhihuang.*?#		0000a049bb8a8ddac0a9b775290228d2f5cb6ecc096e3f771a11f6321945c558
	zhihuang)m?#		000049ed71d58f72b44c9542eef9333df4b17d817a44f455c41bd21005f06131
	zhihuangioM#		0000d047adc628b7719ccc50223d9b0bf417ceb07f2fb2d75446365340ee3212
	zhihuang.>^#		000065cd496117ce73b4ac0e9adfe2dbff69195f2333b11a467d80a989bf9d52
	zhihuangY|Q"		0000953f3926c2082f881db2fa377bfee4c793df64ff85d1b615326515d8728f   remote client: 	akka.tcp://clientSystem@192.168.93.1:2552/user/clientMaster
	zhihuangj?V"		0000363d553d11b6373d441f6f852d96de06e935910402e8240a65af215bbacc   remote client: 	akka.tcp://clientSystem@192.168.93.1:2552/user/clientMaster
	zhihuangY|Q"		0000953f3926c2082f881db2fa377bfee4c793df64ff85d1b615326515d8728f   remote client: 	akka.tcp://clientSystem@192.168.93.1:2552/user/clientMaster
	zhihuangj?V"		0000363d553d11b6373d441f6f852d96de06e935910402e8240a65af215bbacc   remote client: 	akka.tcp://clientSystem@192.168.93.1:2552/user/clientMaster
	zhihuang%?i"		0000378bcfc3b853fa73a235ce07c46d20347a00bb1be5af9e9b6c8c51bf0330   remote client: 	akka.tcp://clientSystem@192.168.93.1:2552/user/clientMaster
	zhihuangyG]#		0000687f99d0a6c29ded56b7f369c6fa923ee07cda139e3787206de3093f8020
	zhihuang+3]$		00006a29703d77b2061eb02900a5968641cd0e1606d62c2ccf3ecc5d3378fcfe
	zhihuangzP\$		00002458135a2884d682e92bae03bd1a8a2fe8205fcf50e556c919fe1ae001c4
	¡­
	¡­

	The time: 
		real	1m2.108s
		user	3m28.395s

	The result of 5 heading 0s

	zhihuang_Y&)		000007c6d9511209a001824712215e098367e7d4c9552f6723d8ae1415c961fe
	zhihuang=jP)		00000b2b343a1d1c21d20a36d0e70dab2f6644e18230a2013b906eb32d23a3f1
	zhihuangdG+*		00000004045f8b8249e7eb43af5f989b2363429c864290c030125ba03daced15
	zhihuangU+o+		00000f430f7c1e0a27e9e24ba78e3c2b931d492b151585c79f7092fd90168871
	zhihuang\;H.		00000d734a3ee3740d887d1028dd43a5e9fec35c698aad9c624cad3aa5e8f0ed
	zhihuang0ER0		00000e13f274651dce93f2dad304d1e4d2a4ae6272a521f96477689eb06a0a15
	zhihuang/p>1		000003cb128505f298e66fb43e9d5e65aa976c02a91adcd795de248f2fd6e93c
	zhihuangeVj1		000003a1f5d6fadd60b552a7e7324299f38c62c0142ffe229df25023d397a619
	zhihuangxbF4		00000952b4bf4d77323934f9e8a7fb0fa872f70d0c75f87f23a0c3566e20d17c
	zhihuang\`K5		00000c37002adb4bba1b0090fe04741f24c8d5a60de255c1ef1ba015db25c433
	zhihuang2\V8		00000c08655fa72364a5eac921349497765ae9d11a55e30ee3c4c48ec246991f
	zhihuangQJ]:		00000db28b3099a73c84b8ce741910b274a3c2031c8c825a5242e14032936047
	zhihuangz"F=		00000f9e86601311ece6bf706d5f7486c72afc6397fcd806727cae0687e9a062
	zhihuangRrR=		000001edcc7b810c9e5bd0473c5f3aacad362845b7585c2347b97021b3edb1aa
	zhihuangKjV=		00000ba3e4c29c3d5a2749d513d8c6b21452e283b3070ab3f80e085bd04e3e07
	zhihuangj,?;		00000af2c3c77408efce6a25cbe5b8fe80046e424a50521885436dd6db78ed62   remote client: 	akka.tcp://clientSystem@192.168.93.1:2552/user/clientMaster
	zhihuang^~+>		00000106382d0ad676ba08e5ad831d03b790dec0658c7088fc78bc638ef617f9
	zhihuangx.,>		000005fd819db7b0135b60b76671de4e85447c1f219c9f9cfaa6522b73b7480b
	zhihuangasw>		000003b866e7cadf0d5b83983d705837fd2632c7df398eeaa256d2bf7e3d783a
	¡­
	¡­

	The time:
		real	4m12.168s
		user	14m21.533s


----------------------------------------------------------------------------------------
Parallelism on Different Machine
----------------------------------------------------------------------------------------

	The program is tested on different granularities.

	1. Local machine using multiple processes.
	2. Two machines with 8 cores.
	3. Three machines with 12 cores.

	In the program logic, there is no restriction on the number of client workers. 

	Each time when a client want to join the system, it will retrieve the remote server master, and send ¡°JOIN¡± message to remote server then the server master 	will calculate a work load for this client. In the client, there is a client master which can distribute the work load to the client local workers.

