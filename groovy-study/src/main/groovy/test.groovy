public static void main(def args){
	// TODO Auto-generated method stub
	def greeting ='hello'
	def clos = {param->println "${greeting} ${param}"}
	clos.call('world')
	
	greeting ='Welcome'
	clos.call('world')
	
	println this.class
	demo(clos)
}

def demo(clo){
	def greeting ='Bonjour'
	clo.call('ken')
}