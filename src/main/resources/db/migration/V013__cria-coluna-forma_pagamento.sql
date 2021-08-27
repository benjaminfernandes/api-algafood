ALTER TABLE forma_pagamento ADD data_atualizacao datetime null;
UPDATE forma_pagamento SET data_atualizacao = utc_timestamp;
