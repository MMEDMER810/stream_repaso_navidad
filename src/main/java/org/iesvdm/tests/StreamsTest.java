package org.iesvdm.tests;

import org.iesvdm.streams.*;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.fail;

class StreamsTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}


	@Test
	void testSkeletonCliente() {
	
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			list.forEach(System.out::println);
		
			

		
			cliHome.commitTransaction();
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	

	@Test
	void testSkeletonComercial() {
	
		ComercialHome comHome = new ComercialHome();	
		try {
			comHome.beginTransaction();
		
			List<Comercial> list = comHome.findAll();		
			list.forEach(System.out::println);		

			comHome.commitTransaction();
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	@Test
	void testSkeletonPedido() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
			list.forEach(System.out::println);	
						

			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	/**
	 * 1. Devuelve un listado de todos los pedidos que se realizaron durante el año 2017, 
	 * cuya cantidad total sea superior a 500€.
	 * @throws ParseException 
	 */
	@Test
	void test1() throws ParseException {
		
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
			
			//PISTA: Generación por sdf de fechas
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2016 = sdf.parse("2016-12-31");
			
			List<Pedido> list = pedHome.findAll();
				
			List<String> res = list.stream()
					.filter(p->p.getFecha().after(ultimoDia2016) && p.getTotal() > 500)
					.map(p -> "ID:"+p.getId()+", Precio: "+p.getTotal()+", Fecha: "+p.getFecha()+", Nombre cliente: "+p.getCliente().getNombre())
					.toList();

			res.forEach(System.out::println);
						
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 2. Devuelve un listado con los identificadores de los clientes que NO han realizado algún pedido. 
	 * 
	 */
	@Test
	void test2() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			var res = list.stream()
					.filter(c->c.getPedidos().toArray().length == 0)
					.map(c->c.getId())
					.toList();

			res.forEach(System.out::println);
		
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 3. Devuelve el valor de la comisión de mayor valor que existe en la tabla comercial
	 */
	@Test
	void test3() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
			comHome.beginTransaction();
		
			List<Comercial> list = comHome.findAll();		
			
			var res = list.stream()
					.sorted(comparing(Comercial::getComisión).reversed())
					.limit(1)
					.map(c->"Comisión más alta: "+c.getComisión())
					.toList();

			res.forEach(System.out::println);
				
			comHome.commitTransaction();

			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 4. Devuelve el identificador, nombre y primer apellido de aquellos clientes cuyo segundo apellido no es NULL. 
	 * El listado deberá estar ordenado alfabéticamente por apellidos y nombre.
	 */
	@Test
	void test4() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			

			var res = list.stream()
							.filter(c->c.getApellido2() != null)
									.map(c-> "ID: "+c.getId() + ", Nombre: "+c.getNombre()+", apellido1: "+c.getApellido1()+", Apellido2: "+c.getApellido2())
											.toList();

			res.forEach(System.out::println);
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 5. Devuelve un listado con los nombres de los comerciales que terminan por "el" o "o". 
	 *  Tenga en cuenta que se deberán eliminar los nombres repetidos.
	 */
	@Test
	void test5() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
			comHome.beginTransaction();
		
			List<Comercial> list = comHome.findAll();		
			
			var res = list.stream()
							.filter(c-> c.getNombre().toLowerCase().substring(c.getNombre().length()-2, c.getNombre().length()).equals("el")
							|| c.getNombre().toLowerCase().substring(c.getNombre().length()-1, c.getNombre().length()).equals("o"))
							.map(c->"Nombre: "+c.getNombre())
							.distinct()
							.toList();

			res.forEach(System.out::println);


			comHome.commitTransaction();
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 6. Devuelve un listado de todos los clientes que realizaron un pedido durante el año 2017, cuya cantidad esté entre 300 € y 1000 €.
	 */
	@Test
	void test6() throws ParseException {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2016 = sdf.parse("2016-12-31");
			Date primerDia2018 = sdf.parse("2018-01-01");

			var res = list.stream()
					.filter(p ->  p.getFecha().after(ultimoDia2016) && p.getFecha().before(primerDia2018)
					&& p.getTotal() >= 300 && p.getTotal() <= 1000)
					.map(p -> "Cliente: "+p.getCliente()+", fecha: "+p.getFecha()+", precio: "+p.getTotal())
					.toList();

			res.forEach(System.out::println);

			pedHome.commitTransaction();
		}
		catch (RuntimeException | ParseException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 7. Calcula la media del campo total de todos los pedidos realizados por el comercial Daniel Sáez
	 */
	@Test
	void test7() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			var res = list.stream()
					.filter(c -> c.getNombre().toLowerCase().equals("daniel") && c.getApellido1().toLowerCase().equals("sáez"))
					.flatMap(c->c.getPedidos().stream())
					.mapToDouble(p -> ((Pedido) p).getTotal())
					.average();

			System.out.println(res.orElse(0.0));


			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 8. Devuelve un listado con todos los pedidos que se han realizado.
	 *  Los pedidos deben estar ordenados por la fecha de realización
	 * , mostrando en primer lugar los pedidos más recientes
	 */
	@Test
	void test8() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();

			var res = list.stream()
					.sorted(comparing(Pedido::getFecha))
					.toList();

			res.forEach(System.out::println);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 9. Devuelve todos los datos de los dos pedidos de mayor valor.
	 */
	@Test
	void test9() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			var res = list.stream()
					.sorted(comparing(Pedido::getTotal).reversed())
					.toList();

			res.forEach(System.out::println);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 10. Devuelve un listado con los identificadores de los clientes que han realizado algún pedido. 
	 * Tenga en cuenta que no debe mostrar identificadores que estén repetidos.
	 */
	@Test
	void test10() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			
			var res = list.stream()
					//En la bbdd id del cliente no puede ser nulo así que no hace falta filtrar
					//.filter(p->p.getCliente() != null)
					.map(p-> p.getCliente().getId())
					.distinct()
					.toList();

			res.forEach(System.out::println);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 * 11. Devuelve un listado con el nombre y los apellidos de los comerciales que tienen una comisión entre 0.05 y 0.11.
	 * 
	 */
	@Test
	void test11() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			var res = list.stream()
					.filter(c->c.getComisión() >= 0.05 && c.getComisión() <= 0.11)
					.map(c->c.getNombre() + " "+c.getApellido1()+" "+c.getApellido2())
					.toList();

			res.forEach(System.out::println);
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 12. Devuelve el valor de la comisión de menor valor que existe para los comerciales.
	 * 
	 */
	@Test
	void test12() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			var res = list.stream()
					.sorted(comparing(Comercial::getComisión))
					.map(c->c.getComisión())
					.limit(1)
					.toList();

			res.forEach(System.out::println);
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 13. Devuelve un listado de los nombres de los clientes que 
	 * empiezan por A y terminan por n y también los nombres que empiezan por P. 
	 * El listado deberá estar ordenado alfabéticamente.
	 * 
	 */
	@Test
	void test13() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		

			var res = list.stream()
							.flatMap(c->c.getPedidos().stream()
									.filter(p -> ((Pedido) p).getCliente().getNombre().substring(0, 1).equals("A")
											&& ((Pedido) p).getCliente().getNombre().substring((((Pedido) p).getCliente().getNombre()).length() -1, (((Pedido) p).getCliente().getNombre()).length()).equals("n")
											|| ((Pedido) p).getCliente().getNombre().substring(0, 1).equals("P"))
							)
					.sorted(comparing(p -> ((Pedido) p).getCliente().getNombre()))
					.map(p ->((Pedido) p).getCliente().getNombre())
					.distinct()
					.toList();

			res.forEach(System.out::println);

			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 14. Devuelve un listado de los nombres de los clientes 
	 * que empiezan por A y terminan por n y también los nombres que empiezan por P. 
	 * El listado deberá estar ordenado alfabéticamente.
	 */
	@Test
	void test14() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();

			var res = list.stream()
					.filter(c -> c.getNombre().substring(0, 1).equals("A") && c.getNombre().substring(c.getNombre().length() -1, c.getNombre().length()).equals("n")
									||  c.getNombre().substring(0, 1).equals("P"))
					.sorted(comparing(Cliente::getNombre))
					.map(c ->c.getNombre())
					.distinct()
					.toList();

			res.forEach(System.out::println);
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 15. Devuelve un listado de los clientes cuyo nombre no empieza por A. 
	 * El listado deberá estar ordenado alfabéticamente por nombre y apellidos.
	 */
	@Test
	void test15() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			var res = list.stream()
							.filter(c->!c.getNombre().substring(0,1).equals("A"))
							.sorted(comparing(Cliente::getNombre).thenComparing(Cliente::getApellido1).thenComparing(Cliente::getApellido2))
							.toList();

			res.forEach(System.out::println);
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 16. Devuelve un listado con el identificador, nombre y los apellidos de todos 
	 * los clientes que han realizado algún pedido. 
	 * El listado debe estar ordenado alfabéticamente por apellidos y nombre y se deben eliminar los elementos repetidos.
	 */
	@Test
	void test16() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			
			var res = list.stream()
					//En la bbdd id del cliente en pedido no puede ser nulo así que no hace falta filtrar, todos han hecho pedido
					//.filter(p->p.getCliente() != null)
					.sorted(comparing(p-> ((Pedido)p).getCliente().getApellido1()).thenComparing(p-> ((Pedido)p).getCliente().getApellido2()!=null?(((Pedido)p).getCliente().getApellido2()):"").thenComparing(p-> ((Pedido)p).getCliente().getNombre()))
					.map(p-> "ID: "+p.getCliente().getId()+", nombre: "+p.getCliente().getNombre()+", apellidos: "+p.getCliente().getApellido1()+" "+p.getCliente().getApellido2())
					.distinct()
					.toList();

			res.forEach(System.out::println);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 * 17. Devuelve un listado que muestre todos los pedidos que ha realizado cada cliente. 
	 * El resultado debe mostrar todos los datos del cliente primero junto con un sublistado de sus pedidos. 
	 * El listado debe mostrar los datos de los clientes ordenados alfabéticamente por nombre y apellidos.
	 * 
Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100]
	Pedido [id=2, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=270.65, fecha=2016-09-10]
	Pedido [id=16, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=2389.23, fecha=2019-03-11]
	Pedido [id=15, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=370.85, fecha=2019-03-11]
Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200]
	Pedido [id=12, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=3045.6, fecha=2017-04-25]
	Pedido [id=7, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=5760.0, fecha=2015-09-10]
	Pedido [id=3, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=65.26, fecha=2017-10-05]
	...
	 */
	@Test
	void test17() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
            var res = list.stream()
                            .sorted(comparing(Cliente::getNombre))
                            .map(c-> c + "\n\t"+c.getPedidos().stream()
                                    .map(p -> ((Pedido)p).toString() + "\n\t")
                                    .collect(Collectors.joining()))
                            .toList();

            res.forEach(System.out::println);

			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 18. Devuelve un listado que muestre todos los pedidos en los que ha participado un comercial. 
	 * El resultado debe mostrar todos los datos de los comerciales y el sublistado de pedidos. 
	 * El listado debe mostrar los datos de los comerciales ordenados alfabéticamente por apellidos.
	 */
	@Test
	void test18() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
		
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();

            var res = list.stream()
                    .filter(c->c.getPedidos() != null)
                    .sorted(comparing(Comercial::getApellido1).thenComparing(Comercial::getApellido2))
                    .map(c->c + "\n\t"+c.getPedidos().stream()
                            .map(p -> ((Pedido)p).toString() + "\n\t")
                            .collect(Collectors.joining()))
                    .toList();

            res.forEach(System.out::println);
		
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 19. Devuelve el nombre y los apellidos de todos los comerciales que ha participado 
	 * en algún pedido realizado por María Santana Moreno.
	 */
	@Test
	void test19() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			
			var res = list.stream()
					.filter(p->p.getCliente().getNombre().toLowerCase().equals("maría")
							&& p.getCliente().getApellido1().toLowerCase().equals("santana")
							&& p.getCliente().getApellido2().toLowerCase().equals("moreno"))
					.map(p->p.getComercial().getNombre() + " " + p.getComercial().getApellido1() + " "+p.getComercial().getApellido2())
					.distinct()
					.toList();

			res.forEach(System.out::println);
			
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	

	/**
	 * 20. Devuelve un listado que solamente muestre los comerciales que no han realizado ningún pedido.
	 */
	@Test
	void test20() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
		
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
		
			var res = list.stream()
					.filter(c->c.getPedidos().isEmpty())
					.toList();

			res.forEach(System.out::println);
			
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 21. Calcula el número total de comerciales distintos que aparecen en la tabla pedido
	 */
	@Test
	void test21() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			var res = list.stream()
					.map(p->p.getComercial())
					.distinct()
					.count();

			System.out.println(res);

			
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 * 22. Calcula el máximo y el mínimo de total de pedido en un solo stream, transforma el pedido a un array de 2 double total, utiliza reduce junto con el array de double para calcular ambos valores.
	 */
	@Test
	void test22() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			Optional<Double[]> res = list.stream()
					.map(p-> new Double[]{p.getTotal(), p.getTotal()})
					.reduce((acc, val) -> new Double[] {
						Math.min(acc[0], val[0]),
						Math.max(acc[1], val[1])
					});

			res.ifPresent(r-> {
				System.out.println("Precio total mínimo: "+r[0]);
				System.out.println("Precio total máximo: "+r[1]);

			});

			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	
	/**
	 * 23. Calcula cuál es el valor máximo de categoría para cada una de las ciudades que aparece en cliente
	 */
	@Test
	void test23() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();

			List<Cliente> list = cliHome.findAll();

			var res = list.stream()
					.filter(c->c.getCategoría() != null)
					.map(c-> Map.entry(c.getCiudad(), c.getCategoría()))
					.sorted((c1, c2) -> c2.getValue() - c1.getValue())
					.toList();

			res.forEach(System.out::println);

			cliHome.commitTransaction();

		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 24. Calcula cuál es el máximo valor de los pedidos realizados 
	 * durante el mismo día para cada uno de los clientes. Es decir, el mismo cliente puede haber 
	 * realizado varios pedidos de diferentes cantidades el mismo día. Se pide que se calcule cuál es 
	 * el pedido de máximo valor para cada uno de los días en los que un cliente ha realizado un pedido. 
	 * Muestra el identificador del cliente, nombre, apellidos, la fecha y el valor de la cantidad.
	 * Pista: utiliza collect, groupingBy, maxBy y comparingDouble métodos estáticos de la clase Collectors
	 */
	@Test
	void test24() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			var res = list.stream()
					.collect(Collectors.groupingBy(
							Pedido::getCliente,
							Collectors.groupingBy(
									Pedido::getFecha,
									Collectors.maxBy(Comparator.comparingDouble(Pedido::getTotal))
									)
							)
					);

			res.forEach((cliente, maximosPorFecha) ->
					maximosPorFecha.forEach((fecha, pedido) ->
							System.out.println("Cliente: "
											+ cliente.getId() + " "+cliente.getNombre()+" "+cliente.getApellido1()+ " "
											+cliente.getApellido2() +
									", Fecha: " + fecha +
									", Pedido de máximo valor: " + pedido.get().getTotal())
					)
			);

			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 *  25. Calcula cuál es el máximo valor de los pedidos realizados durante el mismo día para cada uno de los clientes, 
	 *  teniendo en cuenta que sólo queremos mostrar aquellos pedidos que superen la cantidad de 2000 €.
	 *  Pista: utiliza collect, groupingBy, filtering, maxBy y comparingDouble métodos estáticos de la clase Collectors
	 */
	@Test
	void test25() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			var res = list.stream()
					.filter(p->p.getTotal() > 2000)
					.collect(Collectors.groupingBy(
									Pedido::getCliente,
									Collectors.groupingBy(
											Pedido::getFecha,
											Collectors.maxBy(Comparator.comparingDouble(Pedido::getTotal))
									)
							)
					);

			res.forEach((cliente, maximosPorFecha) ->
					maximosPorFecha.forEach((fecha, pedido) ->
							System.out.println("Cliente: "
									+ cliente.getId() + " "+cliente.getNombre()+" "+cliente.getApellido1()+ " "
									+cliente.getApellido2() +
									", Fecha: " + fecha +
									", Pedido de máximo valor: " + pedido.get().getTotal())
					)
			);
			
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 *  26. Devuelve un listado con el identificador de cliente, nombre y apellidos 
	 *  y el número total de pedidos que ha realizado cada uno de clientes durante el año 2017.
	 * @throws ParseException 
	 */
	@Test
	void test26() throws ParseException {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			//PISTA: Generación por sdf de fechas
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2016 = sdf.parse("2016-12-31");
			Date primerDia2018 = sdf.parse("2018-01-01");

			var res = list.stream()
					.map(c->"Cliente: Id="+c.getId()+ " "+c.getNombre()+" "+c.getApellido1()+" "+c.getApellido2()+
							" - Número de pedidos en 2017: "+
							c.getPedidos().stream()
									.filter(p-> ((Pedido)p).getFecha().after(ultimoDia2016)
									&& ((Pedido)p).getFecha().before(primerDia2018)).count())
					.toList();

			res.forEach(System.out::println);

			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 27. Devuelve cuál ha sido el pedido de máximo valor que se ha realizado cada año. El listado debe mostrarse ordenado por año.
	 */
	@Test
	void test27() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			Calendar calendar = Calendar.getInstance();
			
			var res = list.stream()
					.collect(Collectors.groupingBy(p -> p.getFecha().toString().substring(0,4)
							, Collectors.maxBy(Comparator.comparing(Pedido::getTotal)))
					);


			res.forEach((fecha, pedido) ->
					pedido.ifPresent((p) ->
							System.out.println(fecha+": "+p)
					)
			);

			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	
	/**
	 *  28. Devuelve el número total de pedidos que se han realizado cada año.
	 */
	@Test
	void test28() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			Calendar calendar = Calendar.getInstance();
			
			var res = list.stream()
					.collect(Collectors.groupingBy(p -> p.getFecha().toString().substring(0,4)
							, Collectors.counting())
					);


			res.forEach((fecha, pedidos) ->
							System.out.println(fecha + ": "+pedidos+" pedidos en total" )
					);


			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 *  29. Devuelve los datos del cliente que realizó el pedido
	 *  
	 *   más caro en el año 2019.
	 * @throws ParseException 
	 */
	@Test
	void test29() throws ParseException {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			//PISTA: Generación por sdf de fechas
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2018 = sdf.parse("2018-12-31");
			Date primerDia2020 = sdf.parse("2020-01-01");
			
			var res = list.stream()
					.filter(p->p.getFecha().after(ultimoDia2018) && p.getFecha().before(primerDia2020))
					.sorted((p1, p2) -> (int)p2.getTotal()-(int)p1.getTotal())
					.limit(1)
					.map(p->p.getCliente()+ ". Pedido más caro en 2019: "+p.getTotal()+"€")
					.toList();

			res.forEach(System.out::println);
			
				
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	
	/**
	 *  30. Calcula la estadísticas de total de todos los pedidos.
	 *  Pista: utiliza collect con summarizingDouble
	 */
	@Test
	void test30() throws ParseException {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
			
			DoubleSummaryStatistics res = list.stream()
							.collect(Collectors.summarizingDouble(p-> p.getTotal()));

			System.out.println(res);
				
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
}
