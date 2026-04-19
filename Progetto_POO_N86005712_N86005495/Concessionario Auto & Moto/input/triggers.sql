-- TRIGGER: un veicolo venduto non può essere rivenduto
CREATE OR REPLACE FUNCTION check_veicolo_disponibile()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM Vendita WHERE veicolo = NEW.veicolo) THEN
        RAISE EXCEPTION 'Il veicolo è già stato venduto';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_veicolo_venduto
BEFORE INSERT ON Vendita
FOR EACH ROW EXECUTE FUNCTION check_veicolo_disponibile();

-- TRIGGER: un cliente può fare massimo 3 test drive al mese
CREATE OR REPLACE FUNCTION check_testdrive_limite()
RETURNS TRIGGER AS $$
DECLARE
    count_td INT;
BEGIN
    SELECT COUNT(*) INTO count_td FROM TestDrive
    WHERE cliente = NEW.cliente AND date_part('month', data) = date_part('month', NEW.data)
    AND date_part('year', data) = date_part('year', NEW.data);

    IF count_td >= 3 THEN
        RAISE EXCEPTION 'Limite massimo di 3 test drive al mese raggiunto';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_testdrive
BEFORE INSERT ON TestDrive
FOR EACH ROW EXECUTE FUNCTION check_testdrive_limite();
