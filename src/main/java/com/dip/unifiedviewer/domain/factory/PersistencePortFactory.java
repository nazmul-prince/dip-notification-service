package com.dip.unifiedviewer.domain.factory;

import com.dip.unifiedviewer.constansts.PublicApiWorkerTaskType;
import com.dip.unifiedviewer.domain.persistent.services.DomainPersistenceServicePort;

public interface PersistencePortFactory {

  DomainPersistenceServicePort getPortImpl(PublicApiWorkerTaskType taskType);
}
