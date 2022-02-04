package it.conto.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.conto.pojo.ContoCorrente;
import it.conto.pojo.Movimento;



@Path("/conti")
public class GestioneEwallet {

	private static List<ContoCorrente> conti = new ArrayList<ContoCorrente>();
	private static List<Movimento> movimenti = new ArrayList<Movimento>();

	@GET
	@Path("/mostraconti")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ContoCorrente> retrieveConti(){
		return conti;
	}

	@GET
	@Path("/mostramovimenti")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Movimento> retrieveMovimenti(){
		return movimenti;
	}
	
	@DELETE
	@Path("/elimina/{iban}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response eliminaContoCorrente(@PathParam("iban")int iban) {
	
		
		for(ContoCorrente cont : conti) {
			if(cont.getIban() == iban) {
				conti.remove(cont);
				break;
			}
				
		}
		
		return Response.status(200).entity("Eliminazione ContoCorrente avvenuta con successo").build();
	}


	@GET
	@Path("/{iban}")
	@Produces(MediaType.APPLICATION_JSON)
	public ContoCorrente retrieveByPk1(@PathParam("iban")int iban) {
		ContoCorrente c = null;
		for(ContoCorrente cont : conti) {
			if(cont.getIban() == iban) {
				c = cont;
			}
		}
		return c;
	}


	@POST
	@Path("/inserisci")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response inserisciContoCorrente(ContoCorrente c) {
		conti.add(c);
		return Response.status(200).entity("Inserimento ContoCorrente avvenuto con successo").build();
	}

	@PUT
	@Path("/aggiorna")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response aggiornaContoCorrente(ContoCorrente c) {
		
		for(ContoCorrente cont : conti) {
			if(cont.getIban() == c.getIban()) {
				int index = conti.lastIndexOf(cont);
				conti.set(index, c);
			}
		
		}
		
		return Response.status(200).entity("Aggiornamento ContoCorrente avvenuto con successo").build();
	}

	@PUT
    @Path("/preleva/{importo}/{iban}/{dataM}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response preleva( @PathParam("importo")double importo,@PathParam("iban")int iban ,@PathParam("dataM")String dataM) {
        for(ContoCorrente c : conti) {
            if(c.getIban()== iban) {
    double nuovoSaldo = c.getSaldo()-importo;
            c.setSaldo(nuovoSaldo);
            Movimento m = new Movimento();
            m.setImporto(importo);
            m.setTipo("prelievo");
            m.setIbanM(iban);
            m.setData(dataM); 
            movimenti.add(m);
               
            }
        
        }
        return Response.status(200).entity("Prelievo effettuato").build();    
    }
   
	@PUT
    @Path("/versa/{importo}/{iban}/{dataM}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response versamento(@PathParam("importo")double importo,@PathParam("iban")int iban , @PathParam("dataM")String dataM) {
        for(ContoCorrente c : conti) {
            if(c.getIban()== iban) {
    double nuovoSaldo = c.getSaldo()+importo;
            c.setSaldo(nuovoSaldo);
            Movimento m = new Movimento();
            m.setImporto(importo);
            m.setTipo("versamento");
            m.setIbanM(iban);
            m.setData(dataM);
            movimenti.add(m);
               
                }
        
            }
        return Response.status(200).entity("Versamento effettuato").build();    
        }

}
