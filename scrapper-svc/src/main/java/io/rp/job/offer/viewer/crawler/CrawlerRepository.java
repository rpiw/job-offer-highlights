package io.rp.job.offer.viewer.crawler;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CrawlerRepository extends JpaRepository<URLEntity, Long> {
}
