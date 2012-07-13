package dsl

import org.joda.time.DateTime
import org.joda.time.Hours

import ar.edu.utn.tadp.empresa.Empresa.*
import ar.edu.utn.tadp.propiedad.Propiedad
import ar.edu.utn.tadp.requerimiento.Requerimiento

class EmpresaDSL {
	
	def cantidad
	def requerimientos = new ArrayList()
	def empresa 
	def host
	def reunion
	def duracion
	
	def EmpresaDSL(unaEmpresa){
		empresa = unaEmpresa
	}
	
	def anfitrion(anfitrion){
		host = anfitrion
		this
	}
	
	def deDuracion(duracion) {
		this.duracion = duracion
		this
	}
	
	def planificarReunion(){
		reunion = empresa.createReunion(host, requerimientos, duracion, DateTime.now().plusDays(2))
		this
	}
	
	def con(cuantos){
		cantidad = cuantos
		this
	}

	def cancelar(block){
		block
		this
	}
	
	def porcentajeDeAsistenciaMenorA(numero){
		reunion.addTratamientoPorAsistenciaMinima(numero)
	}
	
	def projectManagerCancela(){
		reunion.addTratamientoPorObligatoriedad()
	}
	
	def buscar(rol, proyecto){
		con(rol,proyecto)
	}
	
	def replanificar(){
		reunion.agregarTratamientoPorReplanificacion()
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//++ Personas ++++++++++++++++++++++++++++++++++++++++++
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++
	def sector(sector){
		agregarRequerimiento(cantidad, [new Propiedad("Sector", sector)])
		this
	}
	def gerente(){
		agregarRequerimiento(cantidad, [new Propiedad("rol", "Gerente")])
		this
	}
	def programador(){
		agregarRequerimiento(cantidad, [new Propiedad("rol","programador")])
		this
	}
	
	def programador(proyecto){
		agregarRequerimiento(cantidad, [new Propiedad("proyecto",proyecto), new Propiedad("rol","programador")])
		this
	}

	def liderTecnico(){
		agregarRequerimiento(cantidad, [new Propiedad("rol","Lider Tecnico")])
		this
	}
	
	def liderTecnico(proyecto){
		agregarRequerimiento(cantidad, [new Propiedad("proyecto",proyecto), new Propiedad("rol","Lider Tecnico")])
		this
	}
	
	def projectLeader(proyecto){
		agregarRequerimiento(cantidad, [new Propiedad("proyecto",proyecto), new Propiedad("rol","project leader")])
		this
	} 
	
	def diseniadorGrafico(){
		agregarRequerimiento(cantidad, [new Propiedad("rol","graphic designer")])
		this
	}

	def diseniadorGrafico(proyecto){
		agregarRequerimiento(cantidad, [new Propiedad("proyecto",proyecto), new Propiedad("rol","graphic designer")])
		this
	}

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//++ otros recursos ++++++++++++++++++++++++++++++++++++
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	def proyector(){
			agregarRequerimiento(cantidad, [new Propiedad("tipo","proyector")])
		this
	}

	def notebook(){
			agregarRequerimiento(cantidad, [new Propiedad("tipo","notebook")])
		this
	}
	
	def agregarRequerimiento(cant, propiedades){
		cant.with {requerimientos << new Requerimiento(propiedades)}
	}
	
}
