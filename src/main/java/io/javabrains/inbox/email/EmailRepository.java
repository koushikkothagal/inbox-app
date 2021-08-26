package io.javabrains.inbox.email;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface EmailRepository extends CassandraRepository<Email, String>  {

}
