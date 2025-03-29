ALTER SEQUENCE bill_seq_id RESTART WITH 1;

INSERT INTO tb_bill (id, title, appellant_type, amount, due_day_of_month, business_day_of_month, adjust_to_next_business_day) VALUES
(nextval('bill_seq_id'), 'Conta de Telefone', 'MONTHLY', NULL, NULL, 5, NULL);

INSERT INTO public.tb_bill_categories(bill_id, category) VALUES
((SELECT id FROM tb_bill WHERE title = 'Conta de Telefone'), 'SUBSCRIPTIONS');
