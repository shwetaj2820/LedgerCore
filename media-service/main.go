package main

import (
	"context"
	"errors"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/shwetaj2820/LedgerCore/media-service/internal/server"
)

func main() {
	handler := server.New()

	httpServer := &http.Server{
		Addr:         ":8081",
		Handler:      handler,
		ReadTimeout:  5 * time.Second,
		WriteTimeout: 10 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	ctx, stop := signal.NotifyContext(
		context.Background(),
		os.Interrupt,
		syscall.SIGTERM,
	)
	defer stop()

	go func() {
		log.Println("Media service running on port 8081")

		err := httpServer.ListenAndServe()

		if err != nil && !errors.Is(err, http.ErrServerClosed) {
			log.Fatal(err)
		}
	}()

	<-ctx.Done()

	log.Println("Media service shutting down")

	shutdownCtx, cancel := context.WithTimeout(
		context.Background(),
		10*time.Second,
	)
	defer cancel()

	err := httpServer.Shutdown(shutdownCtx)

	if err != nil {
		log.Fatal("server shutdown failed: ", err)
	}

	log.Println("Media service stopped")
}
