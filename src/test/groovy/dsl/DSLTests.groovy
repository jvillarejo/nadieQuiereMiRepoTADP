package dsl

import static org.junit.Assert.*

import ar.edu.utn.tadp.empresa.Empresa
import ar.edu.utn.tadp.empresa.GeneradorDeContexto
import ar.edu.utn.tadp.propiedad.Propiedad
import ar.edu.utn.tadp.requerimiento.Requerimiento
import dsl.main.EmpresaDSL


import org.joda.time.DateTime
import org.joda.time.Hours
import org.junit.Test
import org.junit.Before

import com.google.common.collect.Lists


class DSLTests {

	def empresaDSL
	def empresa
	def programador = new GeneradorDeContexto().newProgramador(new Propiedad("proyecto", "Mobiliame"), Propiedad.empty(), Propiedad.empty())
	def host = new GeneradorDeContexto().newGerente(Propiedad.empty(), Propiedad.empty(), Propiedad.empty())
	def sala = new GeneradorDeContexto().newSala(Propiedad.empty())
	
	@Before
	void setUp(){
		empresa = new GeneradorDeContexto().newEmpresa(Lists.newArrayList(host, programador, sala))
		empresaDSL = new EmpresaDSL(empresa)
	}
	
	@Test
	//rompe porque la empresa no tiene recursos de posta todavía... hay que armar todo el contexto y medio es una paja :P
    void testSegundoCoso(){
        def reunion		// No hago nada con esto
		empresaDSL
			.con(1).projectLeader("Mobiliame")
	        .con(1).liderTecnico("Mobiliame")
	        .con(2).diseniadorGrafico()
	        .con(1).proyector()
	        .con(1).notebook() 
			.anfitrion(host)
			.planificar(reunion)
	        .cancelar({
	            porcentajeDeAsistencia.esMenor(70)
	        })
    }
    
    @Test
    void testSegundoCosoPrima(){
        def reunion		//No hago nada con esto.
		
		empresaDSL.
			con 1 projectLeader "Mobiliame"
	        con 1 liderTecnico "Mobiliame"
	        con 1 diseniadorGrafico()
	        con 1 proyector()
	        con 1 notebook()
			anfitrion host
			planificar reunion
	        cancelar si{
	            porcentajeDeAsistencia.esMenor(70)
	        }
    }
	
	@Test
	void testHumilde(){
		
		//reunion En java. Se pueden hacer un par de inlines, pero pierde expresividad.
		def propProgramador = new Propiedad("rol","Programador")
		def propProyectoMobiliame = new Propiedad("proyecto","Mobiliame")
		def requerimiento = new Requerimiento([propProgramador,propProyectoMobiliame])
		def requerimientos = [requerimiento]
		def reunionPosta = empresa.createReunion(host,requerimientos, Hours.THREE, DateTime.now().plusDays(2));
		
		//reunion con el dsl en groovy. La batata de la vida :D
		def reunion 	//no se hace nada con esto, pero me lo pide o se rompe. 
		
//		def reunionGenerada = empresaDSL.anfitrion host con (1,{programador("Mobiliame")}) planificar reunion
		def reunionGenerada = empresaDSL.con 1 programador "Mobiliame" anfitrion host planificar reunion
		

		/*
		 * Es una paja, pero como el equals entre objetos compara por identidad, solo me queda
		 * esto o redefinir equals para la clase que quiero. 
		 * Y no quiero redefinir equals :D
		 * 
		 * OBS: No es EXACTAMENTE la misma reunión... como la reunión tiene efecto en los recursos,
		 * se genera una reunión nueva de las mismas características (cumple los mismos requerimientos)
		 * pero un rato después (para ser precisos, inmediatamente después :D)
		 * 
		 * Cuando comparamos por igual sólo comparamos aquéllo que no tiene que ver con el horario.
		 */
		assertTrue(reunionPosta.getRecursos().containsAll(reunionGenerada.getRecursos()))
		assertTrue(reunionGenerada.getRecursos().containsAll(reunionPosta.getRecursos()))
		assertEquals(reunionPosta.getAnfitrion(), reunionGenerada.getAnfitrion())
		assertTrue(reunionPosta.getTratamientos().containsAll(reunionGenerada.getTratamientos()))
		assertTrue(reunionGenerada.getTratamientos().containsAll(reunionPosta.getTratamientos()))
		
		}
	}
