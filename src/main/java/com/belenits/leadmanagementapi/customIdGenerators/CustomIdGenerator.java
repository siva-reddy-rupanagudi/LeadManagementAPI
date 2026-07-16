package com.belenits.leadmanagementapi.customIdGenerators;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

@SuppressWarnings("deprecation")
public class CustomIdGenerator implements IdentifierGenerator {
    private static  String prefix;
    public CustomIdGenerator(IdGenerator config) {
        this.prefix = config.prefix();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Number id = (Number) session.createNativeQuery("select next_val from couseq for update ").getSingleResult();
        session.createNativeQuery("update couseq set next_val = next_val + 1 where next_val = " + id).executeUpdate();
        return prefix+id.intValue();
    }
}
