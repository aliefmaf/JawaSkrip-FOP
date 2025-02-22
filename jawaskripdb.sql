--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.account (
    account_id integer NOT NULL,
    acc_amount numeric(15,2) DEFAULT 0.00,
    user_id integer NOT NULL
);


ALTER TABLE public.account OWNER TO postgres;

--
-- Name: account_account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.account_account_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.account_account_id_seq OWNER TO postgres;

--
-- Name: account_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.account_account_id_seq OWNED BY public.account.account_id;


--
-- Name: loan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loan (
    loan_id integer NOT NULL,
    user_id integer,
    principal_amount numeric(15,2) NOT NULL,
    annual_interest_rate numeric(5,2) NOT NULL,
    repayment_period numeric NOT NULL,
    payment_per_period numeric(15,2) NOT NULL,
    total_repayment numeric(15,2) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    loan_status character varying(100) DEFAULT 'Active'::character varying,
    total_amount_paid numeric(10,2) DEFAULT 0,
    pay_interval character varying(20) DEFAULT 'Monthly'::character varying NOT NULL,
    remaining_amount numeric(10,2) DEFAULT 0
);


ALTER TABLE public.loan OWNER TO postgres;

--
-- Name: loan_loan_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.loan_loan_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.loan_loan_id_seq OWNER TO postgres;

--
-- Name: loan_loan_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.loan_loan_id_seq OWNED BY public.loan.loan_id;


--
-- Name: loan_repayment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loan_repayment (
    repayment_id integer NOT NULL,
    loan_id integer,
    payment_amount numeric(15,2) NOT NULL,
    payment_date timestamp without time zone NOT NULL
);


ALTER TABLE public.loan_repayment OWNER TO postgres;

--
-- Name: loan_repayment_repayment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.loan_repayment_repayment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.loan_repayment_repayment_id_seq OWNER TO postgres;

--
-- Name: loan_repayment_repayment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.loan_repayment_repayment_id_seq OWNED BY public.loan_repayment.repayment_id;


--
-- Name: profile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.profile (
    user_id integer NOT NULL,
    username character varying(255) NOT NULL,
    password text NOT NULL
);


ALTER TABLE public.profile OWNER TO postgres;

--
-- Name: profile_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.profile_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.profile_user_id_seq OWNER TO postgres;

--
-- Name: profile_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.profile_user_id_seq OWNED BY public.profile.user_id;


--
-- Name: savings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.savings (
    savings_id integer NOT NULL,
    user_id integer NOT NULL,
    svg_status boolean DEFAULT false,
    svg_percentage numeric(5,2) DEFAULT 0,
    svg_amount numeric(10,2) DEFAULT 0.00
);


ALTER TABLE public.savings OWNER TO postgres;

--
-- Name: savings_savings_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.savings_savings_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.savings_savings_id_seq OWNER TO postgres;

--
-- Name: savings_savings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.savings_savings_id_seq OWNED BY public.savings.savings_id;


--
-- Name: savings_transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.savings_transaction (
    svg_transaction_id integer NOT NULL,
    savings_id integer NOT NULL,
    amount_saved numeric(10,2) NOT NULL,
    transaction_date timestamp without time zone DEFAULT now(),
    transaction_date_only date DEFAULT CURRENT_DATE
);


ALTER TABLE public.savings_transaction OWNER TO postgres;

--
-- Name: savings_transaction_svg_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.savings_transaction_svg_transaction_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.savings_transaction_svg_transaction_id_seq OWNER TO postgres;

--
-- Name: savings_transaction_svg_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.savings_transaction_svg_transaction_id_seq OWNED BY public.savings_transaction.svg_transaction_id;


--
-- Name: transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transaction (
    transaction_id integer NOT NULL,
    account_id integer,
    transaction_type character varying(20) NOT NULL,
    amount_transacted numeric(15,2) NOT NULL,
    transaction_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    description character varying(100) DEFAULT '-'::character varying NOT NULL,
    transaction_date_only date DEFAULT CURRENT_DATE
);


ALTER TABLE public.transaction OWNER TO postgres;

--
-- Name: transaction_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transaction_transaction_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.transaction_transaction_id_seq OWNER TO postgres;

--
-- Name: transaction_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transaction_transaction_id_seq OWNED BY public.transaction.transaction_id;


--
-- Name: account account_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account ALTER COLUMN account_id SET DEFAULT nextval('public.account_account_id_seq'::regclass);


--
-- Name: loan loan_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan ALTER COLUMN loan_id SET DEFAULT nextval('public.loan_loan_id_seq'::regclass);


--
-- Name: loan_repayment repayment_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan_repayment ALTER COLUMN repayment_id SET DEFAULT nextval('public.loan_repayment_repayment_id_seq'::regclass);


--
-- Name: profile user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profile ALTER COLUMN user_id SET DEFAULT nextval('public.profile_user_id_seq'::regclass);


--
-- Name: savings savings_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.savings ALTER COLUMN savings_id SET DEFAULT nextval('public.savings_savings_id_seq'::regclass);


--
-- Name: savings_transaction svg_transaction_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.savings_transaction ALTER COLUMN svg_transaction_id SET DEFAULT nextval('public.savings_transaction_svg_transaction_id_seq'::regclass);


--
-- Name: transaction transaction_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction ALTER COLUMN transaction_id SET DEFAULT nextval('public.transaction_transaction_id_seq'::regclass);


--
-- Name: account account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (account_id);


--
-- Name: account account_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_user_id_key UNIQUE (user_id);


--
-- Name: loan loan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT loan_pkey PRIMARY KEY (loan_id);


--
-- Name: loan_repayment loan_repayment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan_repayment
    ADD CONSTRAINT loan_repayment_pkey PRIMARY KEY (repayment_id);


--
-- Name: profile profile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT profile_pkey PRIMARY KEY (user_id);


--
-- Name: savings savings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.savings
    ADD CONSTRAINT savings_pkey PRIMARY KEY (savings_id);


--
-- Name: savings_transaction savings_transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.savings_transaction
    ADD CONSTRAINT savings_transaction_pkey PRIMARY KEY (svg_transaction_id);


--
-- Name: savings savings_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.savings
    ADD CONSTRAINT savings_user_id_key UNIQUE (user_id);


--
-- Name: transaction transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (transaction_id);


--
-- Name: profile unique_username; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT unique_username UNIQUE (username);


--
-- Name: account account_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.profile(user_id);


--
-- Name: loan_repayment loan_repayment_loan_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan_repayment
    ADD CONSTRAINT loan_repayment_loan_id_fkey FOREIGN KEY (loan_id) REFERENCES public.loan(loan_id) ON DELETE CASCADE;


--
-- Name: loan loan_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT loan_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.profile(user_id) ON DELETE CASCADE;


--
-- Name: savings_transaction savings_transaction_savings_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.savings_transaction
    ADD CONSTRAINT savings_transaction_savings_id_fkey FOREIGN KEY (savings_id) REFERENCES public.savings(savings_id);


--
-- Name: savings savings_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.savings
    ADD CONSTRAINT savings_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.profile(user_id) ON DELETE CASCADE;


--
-- Name: transaction transaction_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_account_id_fkey FOREIGN KEY (account_id) REFERENCES public.account(account_id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

